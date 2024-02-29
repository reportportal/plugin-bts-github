/*
 * Copyright 2023 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.epam.reportportal.extension.github.entity.validator;

import com.epam.reportportal.extension.util.RequestEntityValidator;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import org.apache.commons.collections4.CollectionUtils;

import static com.epam.ta.reportportal.commons.validation.BusinessRule.expect;
import static com.epam.ta.reportportal.ws.model.ErrorType.UNABLE_INTERACT_WITH_INTEGRATION;
import static java.util.function.Predicate.not;

/**
 * @author RiverSharks
 */
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
