package com.dr.archive.service.internal;

import com.dr.archive.model.to.FieldResultTo;
import com.dr.framework.common.form.core.model.FormData;

import java.util.List;

/**
 * 描述：
 *
 * @author tuzl
 * @date 2020/9/14 11:39
 */
public interface FieldConfigConfigService {
    List<FieldResultTo> testFormData(FormData formData);
}
