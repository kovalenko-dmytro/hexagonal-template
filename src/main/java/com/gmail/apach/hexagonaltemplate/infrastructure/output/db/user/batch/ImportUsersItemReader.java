package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import lombok.Setter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@StepScope
@Setter
public class ImportUsersItemReader implements ItemReader<List<User>> {

    @Value("#{jobExecutionContext}")
    private Map<String, Object> executionContext;

    @Override
    public List<User> read() {
        return (List<User>) executionContext.get("USERS");
    }
}
