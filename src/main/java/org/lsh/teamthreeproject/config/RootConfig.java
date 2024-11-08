package org.lsh.teamthreeproject.config;

import org.lsh.teamthreeproject.dto.BoardDTO;
import org.lsh.teamthreeproject.entity.Board;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // 기본 설정
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT); // STRICT로 설정하여 더 구체적이도록 설정

        // 명시적인 매핑 설정
        TypeMap<Board, BoardDTO> typeMap = modelMapper.createTypeMap(Board.class, BoardDTO.class);
        typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getUser().getUserId(), BoardDTO::setUserId);
            mapper.map(src -> src.getUser().getLoginId(), BoardDTO::setUserLoginId);
        });

        return modelMapper;
    }
}
