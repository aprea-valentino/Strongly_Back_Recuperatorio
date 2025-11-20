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

    @Column(name = "url")
    private String url;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_Problem")
    private Report report;

}