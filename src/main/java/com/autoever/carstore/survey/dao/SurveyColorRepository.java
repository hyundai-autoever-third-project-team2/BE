package com.autoever.carstore.survey.dao;

import com.autoever.carstore.survey.entity.SurveyCarModelEntity;
import com.autoever.carstore.survey.entity.SurveyColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyColorRepository extends JpaRepository<SurveyColorEntity, Long> {

}
