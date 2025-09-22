package br.projetodao.test;

import br.projetodao.dao.DaoFactory;
import br.projetodao.dao.SellerDao;
import br.projetodao.model.Department;
import br.projetodao.model.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Department obj = new Department(1, "Books");
        //System.out.println(obj);
        //Seller seller = new Seller(21, "Bob", "bob@gmail", new Date(), 3000.0, obj);
        //System.out.println(seller);
        Scanner sc = new Scanner(System.in);

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findByYd ===");
        Seller seller1 =  sellerDao.findById(3);
        System.out.println(seller1);


        System.out.println("=== TEST 2: seller findByDepartment ===");
        Department department = new Department(2,null);
        List<Seller> list =  sellerDao.findByDepartment(department);

        for(Seller obj2 : list){
            System.out.println(obj2);
        }
         System.out.println("=== TEST 3: seller findAll ===");
        list =  sellerDao.findAll();
        for(Seller obj2 : list){
            System.out.println(obj2);
        }
        System.out.println("=== TEST 4: Insert ===");
        Seller seller = new Seller(21, "Bob", "bob@gmail", new Date(), 3000.0, department);
        sellerDao.insert(seller);
        System.out.println(seller.getId());

        System.out.println("=== TEST 5: Update ===");
        seller1 = sellerDao.findById(3);
        seller1.setName("Vitória");
        sellerDao.update(seller1);//passar a informação para o banco
        System.out.println("Update complete");

        System.out.println("=== TEST 6: Delete ===");
        System.out.println("\n=== TEST 6: seller delete =====");
        System.out.println("Enter id for delete test: ");
        int id = sc.nextInt();
        sellerDao.deleteById(id);
        System.out.println("Delete completed");

        sc.close();
}}