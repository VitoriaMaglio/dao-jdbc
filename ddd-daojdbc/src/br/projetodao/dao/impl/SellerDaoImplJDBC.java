package br.projetodao.dao.impl;

import br.projetodao.dao.SellerDao;
import br.projetodao.model.Seller;

import java.util.List;

public class SellerDaoImplJDBC implements SellerDao {
//classe que é responsável por implementar regrs de negócio da interface
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
        return null;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}
