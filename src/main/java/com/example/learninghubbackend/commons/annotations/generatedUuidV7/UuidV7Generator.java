package com.example.learninghubbackend.commons.annotations.generatedUuidV7;

import com.example.learninghubbackend.utils.UuidUtil;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class UuidV7Generator extends SequenceStyleGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return UuidUtil.v7();
    }
}
