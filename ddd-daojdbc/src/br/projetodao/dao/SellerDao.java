package br.projetodao.dao;

import br.projetodao.model.Department;
import br.projetodao.model.Seller;

import java.util.List;

public interface SellerDao {

    void insert(Seller obj);
    void update(Seller obj);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();

}
