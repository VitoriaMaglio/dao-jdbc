package br.projetodao.dao.impl;

import br.projetodao.dao.SellerDao;
import br.projetodao.model.Department;
import br.projetodao.model.Seller;
import db.DB;
import db.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoImplJDBC implements SellerDao {
//classe que é responsável por implementar regrs de negócio da interface

    //dependência com a conexão, construtor para forçar a dependência
    private Connection conn;
    public SellerDaoImplJDBC(Connection conn){
        this.conn=conn;
    }
    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;
        try {
          //  st=conn.prepareStatement("INSERT INTO seller\n" +
              //      "                (Name, Email, BirthDate, BaseSalary, DepartmentId)\n" +
              //      "        VALUES\n" +
                //    "                (?, ?, ?, ?, ?)"+
            //Statement.RETURN_GENERATED_KEYS); ERRO NÃO TEM ESSE +
            st = conn.prepareStatement(
                    "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, (new Date(obj.getBirthDate().getTime())));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected >0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }DB.closeResultSet(rs);
            } else {
                throw new DbException("No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException("eRRO");
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        try {
            //  st=conn.prepareStatement("INSERT INTO seller\n" +
            //      "                (Name, Email, BirthDate, BaseSalary, DepartmentId)\n" +
            //      "        VALUES\n" +
            //    "                (?, ?, ?, ?, ?)"+
            //Statement.RETURN_GENERATED_KEYS); ERRO NÃO TEM ESSE +
            st = conn.prepareStatement(
                    "UPDATE seller\n" +
                            "                    SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?\n" +
                            "                            WHERE Id = ?"
            );
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6,obj.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("eRRO");
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement st = null;
        try{
            st=conn.prepareStatement("DELETE FROM seller\n" +
                    "        WHERE Id = ?");
            st.setInt(1,id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }


    }

    @Override
    public Seller findById(Integer id) {
        //buscar todos os campos de vendedor + o nome do departamento com um apelido, faz um join
        //para buscar valores das duas tabelas e coloca uma restrição onde id do vendedor seja igual ?

        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st= conn.prepareStatement("SELECT seller.*,department.Name as DepName  \n" +
                    "FROM seller INNER JOIN department  \n" +
                    "ON seller.DepartmentId = department.Id  \n" +
                    "WHERE seller.Id = ? ");
            st.setInt(1, id);
            rs = st.executeQuery();
            //resultset trás dados em tabela, porém em java criamos objetos associados
            if(rs.next()){
                Department dep = instantiaDepartment(rs);
                Seller seller = instantiaSeller(rs,dep);
                //ao transformar a parte de setar os dados do banco em métodos vc está reutilizando instanciação
                return seller;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        return null;
    }

    private Seller instantiaSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setDepartment(dep);
        return seller;
    }
//nesse dois métodos n fazemos try-catch pq já tratamos a exceção no método principal da busca no banco.
    private Department instantiaDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
//find all sem restrição
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st= conn.prepareStatement("SELECT seller.*,department.Name as DepName  \n" +
                    "FROM seller INNER JOIN department  \n" +
                    "ON seller.DepartmentId = department.Id \n" +
                    "ORDER BY Name ");
            //buscar todos os campos de vendedor + o nome do departamento com um apelido,
            //faz um join das duas tabelas, faz uma restrição de índice de department e imprime com nome
            //aqui vai imprimir todos os vendedores com departamento igual
            rs = st.executeQuery();
            //resultset trás dados em tabela, porém em java criamos objetos associados

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();//guardar todo departamento q for instanciado
            while (rs.next()){
                //reaproveita o department
                Department dep = map.get(rs.getInt("DepartmentId"));//verifica se departamento já existe
                if(dep == null){//instancia
                    dep = instantiaDepartment(rs);
                    map.put(rs.getInt("DepartmentID"),dep);
                }
                //errado instanciar objetos vendedores no qual cada um aponta para o mesmo departamento
//com isso nós temos um só departamento na memória e os vendedores apontando para ele.
                Seller seller = instantiaSeller(rs,dep);
                //ao transformar a parte de setar os dados do banco em métodos vc está reutilizando instanciação
                list.add(seller);

            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st= conn.prepareStatement("SELECT seller.*,department.Name as DepName  \n" +
                    "FROM seller INNER JOIN department  \n" +
                    "ON seller.DepartmentId = department.Id \n" +
                    "WHERE DepartmentId = ? \n" +
                    "ORDER BY Name ");
            //buscar todos os campos de vendedor + o nome do departamento com um apelido,
            //faz um join das duas tabelas, faz uma restrição de índice de department e imprime com nome
            //aqui vai imprimir todos os vendedores com departamento igual
            st.setInt(1, department.getId());
            rs = st.executeQuery();
            //resultset trás dados em tabela, porém em java criamos objetos associados

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();//guardar todo departamento q for instanciado
            while (rs.next()){
               //reaproveita o department
                Department dep = map.get(rs.getInt("DepartmentId"));//verifica se departamento já existe
                if(dep == null){//instancia
                    dep = instantiaDepartment(rs);
                    map.put(rs.getInt("DepartmentID"),dep);
                }
                //errado instanciar objetos vendedores no qual cada um aponta para o mesmo departamento
//com isso nós temos um só departamento na memória e os vendedores apontando para ele.
                Seller seller = instantiaSeller(rs,dep);
                //ao transformar a parte de setar os dados do banco em métodos vc está reutilizando instanciação
                list.add(seller);

            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }

    }


}
