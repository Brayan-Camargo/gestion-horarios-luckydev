package com.gestionhorarios.luckydev.service;

import com.gestionhorarios.luckydev.model.Departamento;
import com.gestionhorarios.luckydev.model.Empleado;
import com.gestionhorarios.luckydev.model.Novedad;
import com.gestionhorarios.luckydev.model.TurnoProgramado;
import com.gestionhorarios.luckydev.model.enums.TipoTurno;
import com.gestionhorarios.luckydev.repository.EmpleadoRepository;
import com.gestionhorarios.luckydev.repository.NovedadRepository;
import com.gestionhorarios.luckydev.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneradorHorarioService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private NovedadRepository novedadRepository;

    public List<Empleado> obtenerPersonalDisponible(Long departamentoId, LocalDate fechaDeInicio, LocalDate fechaFin) {
        List<Empleado> todos = empleadoRepository.findAll().stream()
                .filter(e -> e.getDepartamento().getId().equals(departamentoId))
                .collect(Collectors.toList());

        return todos.stream().filter(empleado -> {
            List<Novedad> novedades = novedadRepository.findByEmpleadoId(empleado.getId());

            boolean tieneConflicto = novedades.stream().anyMatch(n ->
                    (n.getFechaInicio().isBefore(fechaFin) || n.getFechaInicio().isEqual(fechaFin)) &&
                            (n.getFechaFin().isAfter(fechaDeInicio) || n.getFechaFin().isEqual(fechaDeInicio))
                    );
            return !tieneConflicto;
        }).collect(Collectors.toList());
    }

    @Autowired
    private TurnoRepository turnoRepository;

    @Transactional
    public void generarBarridoMensual(Long departamentoId, int mes, int anio) {
        List<Empleado> empleados = empleadoRepository.findAll().stream()
                .filter(e -> e.getDepartamento().getId().equals(departamentoId))
                .collect(Collectors.toList());

        if (empleados.isEmpty()) {
            System.out.println("⚠️ No hay empleados para el depto: " + departamentoId + ". Abortando generación.");
            return;
        }

        empleados.sort(Comparator.comparing(Empleado::getFechaUltimoFinDeSemanaLargo,
                Comparator.nullsFirst(Comparator.naturalOrder())));

        LocalDate inicioMes = LocalDate.of(anio, mes, 1);

        // Aqui se asigna fin largo
        for (int semana = 0; semana < 4; semana++) {
            Empleado afortunado = empleados.get(semana % empleados.size());

            LocalDate viernes = inicioMes.plusWeeks(semana).with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

            boolean esViernesSabado = (semana % 2 == 0);

            if (esViernesSabado) {
                registrarTurnoDescanso(afortunado, viernes, "CALIDAD_DE_VIDA"); // Viernes
                registrarTurnoDescanso(afortunado, viernes.plusDays(1), "CALIDAD_DE_VIDA"); // Sabado
            } else {
                registrarTurnoDescanso(afortunado, viernes.plusDays(1), "CALIDAD_DE_VIDA"); // Sabado
                registrarTurnoDescanso(afortunado, viernes.plusDays(2), "CALIDAD_DE_VIDA"); // Domingo
            }

            afortunado.setFechaUltimoFinDeSemanaLargo(viernes);
            empleadoRepository.save(afortunado);
        }
    }

    private void registrarTurnoDescanso(Empleado emp, LocalDate fecha, String motivo) {
        TurnoProgramado descanso = new TurnoProgramado();
        descanso.setEmpleado(emp);
        descanso.setFechaHoraEntrada(fecha.atStartOfDay());
        descanso.setFechaHoraSalida(fecha.atTime(23, 59));
        descanso.setEsDescanso(true);
        descanso.setEsCalidadDeVida(motivo.equals("CALIDAD_DE_VIDA"));
        descanso.setTipoTurno(TipoTurno.DESCANSO);
        turnoRepository.save(descanso);
    }

    public void asignarDescansosRestantes(Long departamentoId, LocalDate inicioSemana) {
        List<Empleado> empleados = obtenerPersonalDisponible(departamentoId, inicioSemana, inicioSemana.plusDays(6));

        for (Empleado emp : empleados) {
            // Verificar cuantos descansos ya tiene (por Calidad de Vida o Novedades)
            long descansosYaAsignados = contarDescansosEnSemana(emp, inicioSemana);

            if (descansosYaAsignados < 2) {
                int faltantes = (int) (2 - descansosYaAsignados);
                boolean asignadosJuntos = false;

                if (faltantes == 2) {
                    // Buscar 2 días disponibles (Lun-Mar, Mar-Mie, etc.)
                    asignadosJuntos = intentarAsignarDescansosJuntos(emp, inicioSemana);
                }

                // 3. Si no agregar descanso por metodo de goteo donde haya espacio
                if (!asignadosJuntos) {
                    asignarDescansosSeparados(emp, inicioSemana, faltantes);
                    System.out.println("⚠️ Aviso: Por cobertura, " + emp.getNombre() + " tendrá descansos separados.");
                }
            }
        }
    }

    private boolean intentarAsignarDescansosJuntos(Empleado emp, LocalDate lunes) {
        for (int i = 0; i <= 3; i++) {
            LocalDate dia1 = lunes.plusDays(i);
            LocalDate dia2 = lunes.plusDays(i + 1);

            if (hayCoberturaParaDescanso(emp.getDepartamento(), dia1) &&
                    hayCoberturaParaDescanso(emp.getDepartamento(), dia2)) {

                registrarTurnoDescanso(emp, dia1, "SEMANAL_NORMAL");
                registrarTurnoDescanso(emp, dia2, "SEMANAL_NORMAL");
                return true;
            }
        }
        return false;
    }

    private boolean hayCoberturaParaDescanso(Departamento dep, LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay();

        long empleadosTrabajando = empleadoRepository.countByDepartamentoId(dep.getId()) -
                turnoRepository.countDescansosPorFechaYDepto(inicioDia, dep.getId());

        return (empleadosTrabajando - 1) >= dep.getPersonalMinimoRequerido();
    }

    private long contarDescansosEnSemana(Empleado emp, LocalDate inicioSemana) {
        LocalDateTime inicio = inicioSemana.atStartOfDay();
        LocalDateTime fin = inicioSemana.plusDays(6).atTime(23, 59);

        // Contamos los turnos marcados como descanso x semana
        return turnoRepository.findAll().stream()
                .filter(t -> t.getEmpleado().getId().equals(emp.getId()))
                .filter(t -> t.isEsDescanso())
                .filter(t -> (t.getFechaHoraEntrada().isAfter(inicio) || t.getFechaHoraEntrada().isEqual(inicio)) &&
                        (t.getFechaHoraEntrada().isBefore(fin) || t.getFechaHoraEntrada().isEqual(fin)))
                .count();
    }

    private void asignarDescansosSeparados(Empleado emp, LocalDate lunes, int faltantes) {
        int asignados = 0;
        for (int i = 0; i < 7 && asignados < faltantes; i++) {
            LocalDate dia = lunes.plusDays(i);

            // Verificamos si ya tiene algo asignado ese dia
            boolean diaOcupado = tieneTurnoEseDia(emp, dia);

            if (!diaOcupado && hayCoberturaParaDescanso(emp.getDepartamento(), dia)) {
                registrarTurnoDescanso(emp, dia, "SEMANAL_SEPARADO");
                asignados++;
            }
        }
    }

    private boolean tieneTurnoEseDia(Empleado emp, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(23, 59);

        return turnoRepository.findAll().stream()
                .filter(t -> t.getEmpleado().getId().equals(emp.getId()))
                .anyMatch(t -> t.getFechaHoraEntrada().isAfter(inicio.minusSeconds(1)) &&
                        t.getFechaHoraEntrada().isBefore(fin.plusSeconds(1)));
    }

}
