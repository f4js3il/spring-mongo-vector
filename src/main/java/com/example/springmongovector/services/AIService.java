package com.example.springmongovector.services;


import com.example.springmongovector.model.Answer;
import com.example.springmongovector.model.Question;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface AIService {

    Answer getAnswer(Question question);
}
