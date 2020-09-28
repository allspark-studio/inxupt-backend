package com.allsparkstudio.zaixiyou.service;

import com.allsparkstudio.zaixiyou.pojo.form.FeedbackForm;
import com.allsparkstudio.zaixiyou.pojo.vo.ResponseVO;

public interface MetaService {
    ResponseVO feedback(FeedbackForm feedbackForm);

}
