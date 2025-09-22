package br.projetodao.test;

import br.projetodao.dao.DaoFactory;
import br.projetodao.dao.SellerDao;
import br.projetodao.model.Department;
import br.projetodao.model.Seller;

import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Department obj = new Department(1, "Books");
        System.out.println(obj);
        Seller seller = new Seller(21, "Bob", "bob@gmail", new Date(), 3000.0, obj);
        System.out.println(seller);
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller1 =  sellerDao.findById(3);
        System.out.println(seller1);

}}