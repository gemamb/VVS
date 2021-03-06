package es.udc.pa.pa001.apuestas.web.services;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

/**
 * The Class AuthenticationPolicyWorker.
 */
public class AuthenticationPolicyWorker
    implements ComponentClassTransformWorker2 {

  @Override
  public final void transform(final PlasticClass plasticClass,
      final TransformationSupport support, final MutableComponentModel model) {

    processPageAnnotations(plasticClass, model);
    processEventHandlerAnnotations(plasticClass, model);

  }

  /**
   * Read and process restriction on page classes annotated with
   * {@link AuthenticationPolicy} annotation.
   *
   * @param plasticClass
   *          Contains class-specific information used when transforming a raw
   *          component class into an executable component class.
   * @param model
   *          Mutable version of
   *          {@link org.apache.tapestry5.model.ComponentModel} used during the
   *          transformation phase.
   */
  private void processPageAnnotations(final PlasticClass plasticClass,
      final MutableComponentModel model) {

    final AuthenticationPolicy policy = plasticClass
        .getAnnotation(AuthenticationPolicy.class);
    if (policy != null) {
      model.setMeta(AuthenticationValidator.PAGE_AUTHENTICATION_TYPE,
          policy.value().toString());
    }

  }

  /**
   * Inject meta datas about annotated methods.
   *
   * @param plasticClass
   *          Contains class-specific information used when transforming a raw
   *          component class into an executable component class.
   * @param model
   *          Mutable version of
   *          {@link org.apache.tapestry5.model.ComponentModel} used during the
   *          transformation phase.
   */
  private void processEventHandlerAnnotations(final PlasticClass plasticClass,
      final MutableComponentModel model) {

    for (final PlasticMethod method : plasticClass
        .getMethodsWithAnnotation(AuthenticationPolicy.class)) {
      final String methodName = method.getDescription().methodName;
      final AuthenticationPolicy policy = method
          .getAnnotation(AuthenticationPolicy.class);
      final OnEvent event = method.getAnnotation(OnEvent.class);
      if (methodName.startsWith("on") || event != null) {
        final String componentId = extractComponentId(methodName, event);
        final String eventType = extractEventType(methodName, event);
        String authenticationPolicyMeta = AuthenticationValidator.EVENT_HANDLER_AUTHENTICATION_TYPE
            + "-" + componentId + "-" + eventType;

        authenticationPolicyMeta = authenticationPolicyMeta.toLowerCase();
        model.setMeta(authenticationPolicyMeta, policy.value().toString());
      } else {
        throw new RuntimeException(
            "Cannot put AuthenticationPolicy annotation on a "
                + "non event handler method");
      }
    }

  }

  /**
   * This code is taken deliberatly from:
   * http://svn.apache.org/viewvc/tapestry/tapestry5/trunk/tapestry-core/src/
   * main /java/org/apache/tapestry5/internal/transform/OnEventWorker.java?view=
   * markup Returns the component id to match against, or the empty string if
   * the component id is not specified. The component id is provided by the
   * OnEvent annotation or (if that is not present) by the part of the method
   * name following "From" ("onActionFromFoo").
   *
   * @param methodName
   *          the method name
   * @param annotation
   *          the annotation
   * @return the string
   */
  private String extractComponentId(final String methodName,
      final OnEvent annotation) {
    if (annotation != null) {
      return annotation.component();
    }

    // Method name started with "on". Extract the component id, if present.

    final int fromx = methodName.indexOf("From");

    if (fromx < 0) {
      return "";
    }

    return methodName.substring(fromx + 4);
  }

  /**
   * This code is taken deliberatly from:
   * http://svn.apache.org/viewvc/tapestry/tapestry5/trunk/tapestry-core/src/
   * main /java/org/apache/tapestry5/internal/transform/OnEventWorker.java?view=
   * markup Returns the event name to match against, as specified in the
   * annotation or (if the annotation is not present) extracted from the name of
   * the method. "onActionFromFoo" or just "onAction".
   *
   * @param methodName
   *          the method name
   * @param annotation
   *          the annotation
   * @return the string
   */
  private String extractEventType(final String methodName,
      final OnEvent annotation) {
    if (annotation != null) {
      return annotation.value();
    }

    final int fromx = methodName.indexOf("From");

    // The first two characters are always "on" as in "onActionFromFoo".
    if (fromx == -1) {
      return methodName.substring(2);
    }
    return methodName.substring(2, fromx);
  }
}
