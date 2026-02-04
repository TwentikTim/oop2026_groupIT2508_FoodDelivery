package edu.aitu.oop3.repositories;


import java.util.List;

public interface Repository<T, ID> {
   T findById(ID id);
   List<T> findAll();

}
