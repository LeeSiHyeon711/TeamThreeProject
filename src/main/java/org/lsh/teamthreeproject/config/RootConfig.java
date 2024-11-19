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
                .setMatchingStrategy(MatchingStrategies.STRICT); // STRICT로 설정하여 더 구체적이도록 설정(타입 오류 및 버그 방지)

        // 명시적인 매핑 설정
        TypeMap<Board, BoardDTO> typeMap = modelMapper.createTypeMap(Board.class, BoardDTO.class);
        typeMap.addMappings(mapper -> {
            mapper.map(src -> src.getUser().getUserId(), BoardDTO::setUserId);
            mapper.map(src -> src.getUser().getLoginId(), BoardDTO::setUserLoginId);
        });

        return modelMapper;
    }
}
// 복잡한 로직 사용 시 modelMapper 말고 명시적으로 DtoToEntity 나 EntityToDto 를 설정해주세요!
// (가독성 및 정확성 증가. 디버깅 효율 증가를 위해서 입니다)