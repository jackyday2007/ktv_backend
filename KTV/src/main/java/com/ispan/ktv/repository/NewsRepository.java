package com.ispan.ktv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ispan.ktv.bean.News;

public interface NewsRepository extends JpaRepository<News, Integer> {
	    List<News> findByTitleContaining(String keyword);
}
