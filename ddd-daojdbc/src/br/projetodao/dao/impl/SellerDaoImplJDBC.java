package br.projetodao.dao.impl;

import br.projetodao.dao.SellerDao;
import br.projetodao.model.Department;
import br.projetodao.model.Seller;
import db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoImplJDBC implements SellerDao {
//classe que é responsável por implementar regrs de negócio da interface

    //dependência com a conexão, construtor para forçar a dependência
    private Connection conn;
    public SellerDaoImplJDBC(Connection conn){
        this.conn=conn;
    }
    @Override
    public void insert(Seller obj) {
        
    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

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
                Department dep = new Department();
                dep.setId(rs.getInt("DepartmentId"));
                dep.setName(rs.getString("DepName"));
                Seller seller = new Seller();
                seller.setId(rs.getInt("Id"));
                seller.setName(rs.getString("Name"));
                seller.setEmail(rs.getString("Email"));
                seller.setBaseSalary(rs.getDouble("BaseSalary"));
                seller.setBirthDate(rs.getDate("BirthDate"));
                seller.setDepartment(dep);
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

    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}
