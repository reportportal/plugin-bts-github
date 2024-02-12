package com.epam.reportportal.extension.github.entity.validator;

import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class RequestEntityValidatorWrapperTest {

    private final RequestEntityValidatorWrapper validatorWrapper = Mockito.spy(RequestEntityValidatorWrapper.class);

    @Test
    void validatePostTicketFields_shouldThrowError_whenFieldsAreEmpty() {
        Mockito.doNothing().when(validatorWrapper).validateUsingRequestEntityValidator(any());

        var ticketRequest = new PostTicketRQ();
        assertThatThrownBy(() -> validatorWrapper.validatePostTicketRQ(ticketRequest))
                .isExactlyInstanceOf(ReportPortalException.class)
                .hasMessageContaining("External System fields set is empty!");
    }
}
