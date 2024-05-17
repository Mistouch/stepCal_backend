package com.UniProject.Services;

import com.UniProject.Entities.CompletedTask;
import com.UniProject.Repository.CompletedTaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompletedTaskService {

    @Autowired
    private CompletedTaskRepository completedTaskRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void saveTask(CompletedTask task){
        CompletedTask completedTask=completedTaskRepository.findByEmailAndDate(task.getEmail(),task.getDate());
        if(completedTask==null){
            System.out.println(task);
            completedTaskRepository.save(task);
            System.out.println("in task");
        }
        else{
            double burn=completedTask.getCalorie_burn()+task.getCalorie_burn();
            double intake=completedTask.getCalorie_intake()+task.getCalorie_intake();
            completedTaskRepository.updateCalorie_burn(burn, task.getEmail(), task.getDate());
            completedTaskRepository.updateCalorie_intake(intake, task.getEmail(), task.getDate());
        }
        entityManager.flush();

    }
    public CompletedTask getTask(String email, String date){
        return completedTaskRepository.findByEmailAndDate(email,date);
    }
    public List<CompletedTask>taskHistory(String email){
        List<CompletedTask> progress=completedTaskRepository.findAllByEmail(email);
        for(CompletedTask task:progress){
            System.out.println(task);
        }
        return progress;
    }

    @Transactional
    public void updateUpdate(boolean update,String email,String date){
        completedTaskRepository.updateIsUpdate(update,email,date);
    }
}
