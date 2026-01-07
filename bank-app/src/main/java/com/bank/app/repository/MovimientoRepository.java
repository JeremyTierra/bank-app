package com.bank.app.repository;

import com.bank.app.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    
    List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);
    
    @Query("SELECT m FROM Movimiento m " +
           "WHERE m.cuenta.cliente.clienteId = :clienteId " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY m.fecha DESC")
    List<Movimiento> findByClienteAndFechaBetween(
        @Param("clienteId") Long clienteId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );
    
    @Query("SELECT COALESCE(SUM(ABS(m.valor)), 0) FROM Movimiento m " +
           "WHERE m.cuenta.id = :cuentaId " +
           "AND m.valor < 0 " +
           "AND CAST(m.fecha AS date) = CURRENT_DATE")
    BigDecimal sumWithdrawalsToday(@Param("cuentaId") Long cuentaId);
}
