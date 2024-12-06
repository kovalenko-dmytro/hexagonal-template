package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.entity.UserEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImportUsersItemWriter implements ItemWriter<List<UserEntity>> {

    private final UserRepository userRepository;

    @Override
    public void write(@NonNull Chunk<? extends List<UserEntity>> chunk) throws Exception {
        final var users = chunk.getItems().stream().flatMap(List::stream).toList();
        userRepository.saveAll(users);
    }
}
