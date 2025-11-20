package com.uade.tpo.demo.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "problem_forms")
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproblem_forms")
    private Long id;
     @Column(name = "fullname")
    private String fullname;

    @Column(name = "problem")
    private String problem;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportImage> images = new ArrayList<>();

    public void addImage(ReportImage image) {
        if (image == null) return;
        image.setReport(this);
        this.images.add(image);
    }

    public void removeImage(ReportImage image) {
        if (image == null) return;
        image.setReport(null);
        this.images.remove(image);
    }
}
