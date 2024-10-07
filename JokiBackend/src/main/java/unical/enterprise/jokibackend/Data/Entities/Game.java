package unical.enterprise.jokibackend.Data.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter // lombok, genera getter e setter
@Entity(name = "games") // jakarta, fa riferimento alla tabella games
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Double price;

    @Column
    private String imagePath;

    @Column
    private String genre;

    @Column
    private String developer;

    @Column
    private String publisher;

    @Column(columnDefinition = "DATE")
    private Date releaseDate;

    @Column
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Admin admin;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}