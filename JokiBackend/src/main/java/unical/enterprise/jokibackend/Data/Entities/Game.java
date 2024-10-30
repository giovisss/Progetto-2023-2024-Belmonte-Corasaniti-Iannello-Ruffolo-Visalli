package unical.enterprise.jokibackend.Data.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 50, message = "La lunghezza del titolo deve essere compresa tra 3 e 50 caratteri")
    private String title;

    @Column
    @Size(min = 3, max = 500, message = "La lunghezza della descrizione deve essere compresa tra 3 e 500 caratteri")
    private String description;

    @Column
    @Positive(message = "Il prezzo deve essere maggiore di 0")
    private Double price;

    @Column
    private String imagePath;

    @Column
    private String genre;

    @Column
    @Size(min = 3, max = 50, message = "La lunghezza del developer deve essere compresa tra 3 e 50 caratteri")
    private String developer;

    @Column
    @Size(min = 3, max = 50, message = "La lunghezza del developer deve essere compresa tra 3 e 50 caratteri")
    private String publisher;

    @Column(columnDefinition = "DATE")
    private Date releaseDate;

    @Column
    @PositiveOrZero(message = "Lo stock deve essere maggiore o uguale a 0")
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