package br.projetodao.dao;

import br.projetodao.model.Department;

import java.util.List;

public interface DepartmentDao {
    //operações do crud em uma interface só possui as regras do contato e não como implementá-las.

    void insert(Department obj);
    void update(Department obj);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();



}
