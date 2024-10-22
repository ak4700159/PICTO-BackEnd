package picto.com.genenrator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Category {
    @Id
    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @OneToMany(mappedBy = "category")
    private Set<PhotoRecord> photoRecords = new LinkedHashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Tag> tags = new LinkedHashSet<>();



}