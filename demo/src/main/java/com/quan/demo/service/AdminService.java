package com.quan.demo.service;

import com.quan.demo.repository.IUserRepository;
import com.quan.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public long countUsers() {
        return userRepository.count();
    }

    public long countMovies() {
        return movieRepository.count();
    }
}
