package com.rgarmal.springproject.tienda.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.rgarmal.springproject.tienda.dao.ClientesDAO;
import com.rgarmal.springproject.tienda.dao.mappers.ClienteMapper;
import com.rgarmal.springproject.tienda.model.Cliente;

@Repository
public class ClientesDAOimpl extends JdbcDaoSupport implements ClientesDAO {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public Page<Cliente> findAll(Pageable page) {
        /*
         * String query = "select * from Clientes";
         * 
         * List<Cliente> clientes = getJdbcTemplate().query(query, new ClienteMapper());
         * return clientes;
         */

        String queryCount = "select count(1) from Clientes";
        Integer total = getJdbcTemplate().queryForObject(queryCount, Integer.class);

        Order order = !page.getSort().isEmpty() ? page.getSort().toList().get(0) : Order.by("codigo");

        String query = "SELECT * FROM Clientes ORDER BY " + order.getProperty() + " "
                + order.getDirection().name() + " LIMIT " + page.getPageSize() + " OFFSET " + page.getOffset();

        final List<Cliente> clientes = getJdbcTemplate().query(query, new RowMapper<Cliente>() {

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

        });

        return new PageImpl<Cliente>(clientes, page, total);

    }

    @Override
    public Cliente findById(int codigo) {
        String query = "select * from Clientes where codigo = ?";
        Object params[] = { codigo };
        int types[] = { Types.INTEGER };

        Cliente cliente = (Cliente) getJdbcTemplate().queryForObject(query, params, types, new ClienteMapper());

        return cliente;
    }

    @Override
    public void insert(Cliente cliente) {
        String query = "insert into Clientes (nombre,apellidos,dni,direccion,telefono,email,vip) values (?,?,?,?,?,?,?)";
        /*
         * Object[] params = {
         * cliente.getNombre(),
         * cliente.getApellidos(),
         * cliente.getDni(),
         * cliente.getDireccion(),
         * cliente.getTelefono(),
         * cliente.getEmail(),
         * cliente.isVip()
         * };
         * 
         * final int[] types = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
         * Types.VARCHAR, Types.VARCHAR,
         * Types.VARCHAR, Types.BOOLEAN };
         * 
         * int update = getJdbcTemplate().update(query, params, types);
         */

        KeyHolder keyHolder = new GeneratedKeyHolder();

        getJdbcTemplate().update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, cliente.getNombre());
                ps.setString(2, cliente.getApellidos());
                ps.setString(3, cliente.getDni());
                ps.setString(4, cliente.getDireccion());
                ps.setString(5, cliente.getTelefono());
                ps.setString(6, cliente.getEmail());
                ps.setBoolean(7, cliente.isVip());
                return ps;
            }

        }, keyHolder);

        cliente.setCodigo(keyHolder.getKey().intValue());

    }

    @Override
    public void update(Cliente cliente) {
        String query = "update Clientes set nombre = ?, apellidos = ?, dni = ?, direccion = ?, telefono = ?, email = ?, vip = ? where codigo = ?";
        Object[] params = {
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getDni(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.isVip(),
                cliente.getCodigo()
        };

        final int[] types = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.BOOLEAN, Types.INTEGER };

        int update = getJdbcTemplate().update(query, params, types);

    }

    @Override
    public void delete(int codigo) {
        String query = "delete from Clientes where codigo = ?";
        Object[] params = { codigo };

        final int[] types = { Types.INTEGER };

        int update = getJdbcTemplate().update(query, params, types);

    }

}
