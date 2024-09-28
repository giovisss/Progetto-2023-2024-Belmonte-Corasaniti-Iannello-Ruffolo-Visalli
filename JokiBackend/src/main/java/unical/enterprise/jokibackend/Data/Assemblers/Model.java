package unical.enterprise.jokibackend.Data.Assemblers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@RequiredArgsConstructor
public class Model<T> extends RepresentationModel<Model<T>> {
    private final T model;
}
