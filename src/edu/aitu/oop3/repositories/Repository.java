package edu.aitu.oop3.repositories;


public interface Repository<T, ID> {
   T findById(ID id);

}
