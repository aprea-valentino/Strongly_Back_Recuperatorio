package com.uade.tpo.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "problem_images")
@Data
public class ReportImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "idProblem", insertable = false, updatable = false)
    private Long idProblem;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProblem", referencedColumnName = "idproblem_forms")
    private Report report;

}