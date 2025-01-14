package com.autoever.carstore.survey.dao;

import com.autoever.carstore.survey.entity.SurveyCarModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyCarModelRepository extends JpaRepository<SurveyCarModelEntity, Long> {

}
