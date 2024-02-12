package com.epam.reportportal.extension.github.entity.validator;

import com.epam.reportportal.extension.util.RequestEntityValidator;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import org.apache.commons.collections4.CollectionUtils;

import static com.epam.ta.reportportal.commons.validation.BusinessRule.expect;
import static com.epam.ta.reportportal.ws.model.ErrorType.UNABLE_INTERACT_WITH_INTEGRATION;
import static java.util.function.Predicate.not;

public class RequestEntityValidatorWrapper {

    public void validatePostTicketRQ(PostTicketRQ ticketRequest) {
        validatePostTicketFields(ticketRequest);
        validateUsingRequestEntityValidator(ticketRequest);
    }

    void validateUsingRequestEntityValidator(PostTicketRQ ticketRequest) {
        RequestEntityValidator.validate(ticketRequest);
    }

    private static void validatePostTicketFields(PostTicketRQ ticketRequest) {
        expect(ticketRequest.getFields(), not(CollectionUtils::isEmpty)).verify(UNABLE_INTERACT_WITH_INTEGRATION,
                "External System fields set is empty!");
    }

}
