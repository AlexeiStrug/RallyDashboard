package com.ge.dashboard.utils.config.builder;

import com.ge.dashboard.utils.Messages;
import com.ge.dashboard.utils.config.builder.dto.ReleaseDto;
import com.ge.dashboard.utils.config.builder.dto.RemainDto;
import com.ge.dashboard.utils.config.builder.dto.SprintDto;
import com.ge.dashboard.utils.config.builder.enums.ResponseDataType;
import com.ge.dashboard.utils.config.builder.enums.ResponseType;
import com.ge.dashboard.utils.exception.TypeOfResponseTypeProcessingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DtoResponseBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DtoResponseBuilder.class);

    @Autowired
    private Messages messages;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public CustomResponse buildResponse(ResponseType responseType, List<?> list) throws TypeOfResponseTypeProcessingException {

        CustomResponse response = new CustomResponse();

        if (responseType.equals(ResponseType.WARNING)) {
            LOGGER.warn("Setting response status to ERROR");
            response.setResponseType(responseType);
            response.setMessage(messages.get("ERROR_GOT_DATA"));
            response.setResult(null);
        }
        if (responseType.equals(ResponseType.SUCCESS)) {
            LOGGER.debug("Setting data to response");
            response.setResponseType(responseType);
            response.setMessage(messages.get("SUCCESS_GOT_DATA"));
            response.setResult(list);
        } else {
            throw new TypeOfResponseTypeProcessingException("Sorry! Next type of request -> " + responseType + " does not supported yet.");
        }
        LOGGER.debug("Returning response: " + response);
        return response;
    }


    public List<Object> buildResponseDto(List<?> list, ResponseDataType dataType) throws TypeOfResponseTypeProcessingException {
        if (dataType.equals(ResponseDataType.SPRINT)) {
            return Arrays.asList(modelMapper().map(list, SprintDto[].class));
        } else if (dataType.equals(ResponseDataType.RELEASE)) {
            return Arrays.asList(modelMapper().map(list, ReleaseDto[].class));
        } else if (dataType.equals(ResponseDataType.REMAIN)) {
            return Arrays.asList(modelMapper().map(list, RemainDto[].class));
        } else {
            throw new TypeOfResponseTypeProcessingException("Sorry! Next type of request -> " + dataType + " does not supported yet.");
        }
    }
}
