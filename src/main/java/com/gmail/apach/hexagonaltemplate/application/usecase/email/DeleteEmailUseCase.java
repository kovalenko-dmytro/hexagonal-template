package com.gmail.apach.hexagonaltemplate.application.usecase.email;

import com.gmail.apach.hexagonaltemplate.application.port.input.email.DeleteEmailInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.email.DeleteEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.email.GetEmailOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteEmailUseCase implements DeleteEmailInputPort {

    private final GetEmailOutputPort getEmailOutputPort;
    private final DeleteEmailOutputPort deleteEmailOutputPort;

    @Override
    public void deleteByEmailId(String emailId) {
        final var deletedEmail = getEmailOutputPort.getByEmailId(emailId);
        deleteEmailOutputPort.deleteByEmailId(deletedEmail.getEmailId());
    }
}
