package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.entity.UserEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.mapper.UserDbMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImportUsersItemProcessor implements ItemProcessor<List<User>, List<UserEntity>> {

    private final UserDbMapper userDbMapper;

    @Override
    public List<UserEntity> process(@NonNull List<User> users) throws Exception {
        return users.stream().map(userDbMapper::toUserEntity).toList();
    }
}
