package br.com.rooting.roxana.response.processor;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.message.MessageCreatorFactory;
import br.com.rooting.roxana.message.MessageSeverity;
import br.com.rooting.roxana.parameter.finder.ConstraintValidationParameterFinder;
import br.com.rooting.roxana.parameter.finder.ParameterFinderStrategy;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

class ConstraintValidatorResponseProcessor extends ResponseProcessor {

    ConstraintValidatorResponseProcessor(final RoxanaProperties roxanaProperties,
                                         final MessageCreatorFactory messageCreatorFactory,
                                         final ResponseProcessorManager responseCreatorManager) {

        super(roxanaProperties, messageCreatorFactory, responseCreatorManager);
    }

    @Override
    protected Boolean isUnexpectedException(Exception e) {
        return false;
    }

    @Override
    protected HttpStatus getResponseCode(Exception e) {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    protected List<MessageResponseDTO> getMessagesResponseDTO(Exception e) {
        ConstraintViolationException constraintException = (ConstraintViolationException) e;
        return constraintException.getConstraintViolations().stream()
                .map(this::getMessageResponseDTO)
                .collect(Collectors.toList());
    }

    private MessageResponseDTO getMessageResponseDTO(ConstraintViolation<?> c) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setKey(c.getMessageTemplate());
        dto.setSeverity(MessageSeverity.ERROR);
        dto.setParameters(this.getParameterFinderStrategy(c).findParameters());
        return dto;
    }

    private ParameterFinderStrategy getParameterFinderStrategy(ConstraintViolation<?> c) {
        return new ConstraintValidationParameterFinder(c);
    }

}