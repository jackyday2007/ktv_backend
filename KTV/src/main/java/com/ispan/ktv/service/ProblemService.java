package com.ispan.ktv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ispan.ktv.bean.Problems;
import com.ispan.ktv.repository.ProblemsRepository;

@Service
public class ProblemService {
	
	@Autowired
	private ProblemsRepository problemsRepository;
	
	public Problems insertOrUpdateProblem(Problems problem) {
		return problemsRepository.save(problem);
	}
	
	public Problems findProblemById(Integer problemId) {
		Optional<Problems> optional = problemsRepository.findById(problemId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<Problems> findAllProblem(){
		return problemsRepository.findAll();
	}
	
	public void deleteProblemById(Integer problemId) {
		problemsRepository.deleteById(problemId);
	}

}
