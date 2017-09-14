package ee.scanner.tablet.util;

import ee.scanner.tablet.feedback.Feedback;
import ee.scanner.tablet.feedback.FeedbackType;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

public class ControllerUtil {

    /**
     * Adds feedback list to model. This can be later used via Thymeleaf to generate Bootstrap alert dialogs.
     * <p>
     * For example <code>ControllerUtil.setFeedback(FeedbackType.SUCCESS, "Hello world!")</code> will add a list
     * containing one {@link Feedback} object that can be used to generate
     * HTML tag <code><div class="alert alert-success">Hello world!</div></code>
     * <p>
     *
     * @param model    to add feedback
     * @param type     of the feedback
     * @param feedback as message
     * @return model which is updated
     */
    public static Model setFeedback(Model model, FeedbackType type, String feedback) {
        if (model.containsAttribute(Feedback.FEEDBACK_VARIABLE_NAME)) {
            List<Feedback> feedbacks = (List<Feedback>) model.asMap().get(Feedback.FEEDBACK_VARIABLE_NAME);
            feedbacks.add(new Feedback(feedback, type));
            model.addAttribute(Feedback.FEEDBACK_VARIABLE_NAME, feedbacks);
        } else {
            List<Feedback> lst = new ArrayList<>();
            lst.add(new Feedback(feedback, type));
            model.addAttribute(Feedback.FEEDBACK_VARIABLE_NAME, lst);
        }
        return model;
    }
}
