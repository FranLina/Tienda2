package com.rgarmal.springproject.tienda.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.lang.Nullable;

import com.rgarmal.springproject.tienda.model.Cliente;

public class ClienteMapper implements org.springframework.jdbc.core.RowMapper<Cliente> {

    @Override
    @Nullable
    public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setCodigo(rs.getInt("codigo"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setDni(rs.getString("dni"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setVip(rs.getBoolean("vip"));
        return cliente;
    }

}
