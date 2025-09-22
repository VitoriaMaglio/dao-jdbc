package br.projetodao.dao;

import br.projetodao.dao.impl.SellerDaoImplJDBC;
import br.projetodao.model.Seller;
import db.DB;

import java.sql.Connection;
import java.util.List;

public class DaoFactory {
    //classe com métodos estáticos que realização as ações no banco
//tem um método que expoem o tipo da interface e e instancia uma implementação, assim vc escondem a implementação
    public static SellerDao createSellerDao(){
        return new SellerDaoImplJDBC(DB.getConnection()) {

        };
    }
}
