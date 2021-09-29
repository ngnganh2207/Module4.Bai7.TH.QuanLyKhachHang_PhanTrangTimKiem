package com.codegym.repository.impl;//package com.codegym.repository.impl;
//
//import com.codegym.model.Customer;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.NoResultException;
//import javax.persistence.PersistenceContext;
//import javax.persistence.TypedQuery;
//import java.util.List;
//
//@Transactional
//public class CustomerRepository implements com.codegym.repository.impl.ICustomerRepository {
//    @PersistenceContext
//    private EntityManager em;
//
//    @Override
//    public List<Customer> findAll() {
//        TypedQuery<Customer> query=em.createQuery("select c from Customer c", Customer.class);
//        return query.getResultList();
//    }
//
//    @Override
//    public Customer findById(Long id) {
//        TypedQuery<Customer> query=
//                em.createQuery("select c from Customer c where c.id=:id",Customer.class);
//        query.setParameter("id",id);
//        try{
//            return query.getSingleResult();
//        }catch (NoResultException e){
//            return null;
//        }
//
//    }
//
//    @Override
//    public void save(Customer customer) {
//        if(customer.getId()!= null){
//            em.merge(customer);
//        }else {
//            em.persist(customer);
//        }
//    }
//
//    @Override
//    public void remove(Long id) {
//        Customer customer= findById(id);
//        if(customer!= null){
//            em.remove(customer);
//        }
//    }
//}
