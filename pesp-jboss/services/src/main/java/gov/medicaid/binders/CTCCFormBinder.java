/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package gov.medicaid.binders;

import gov.medicaid.domain.model.AttachedDocumentsType;
import gov.medicaid.domain.model.DocumentNames;
import gov.medicaid.domain.model.DocumentType;
import gov.medicaid.domain.model.EnrollmentType;
import gov.medicaid.domain.model.FacilityCredentialsType;
import gov.medicaid.domain.model.ProviderInformationType;
import gov.medicaid.domain.model.StatusMessageType;
import gov.medicaid.domain.model.StatusMessagesType;
import gov.medicaid.entities.Enrollment;
import gov.medicaid.entities.ProviderProfile;
import gov.medicaid.entities.dto.FormError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This binder handles the organization disclosure.
 *
 * @author TCSASSEMBLER
 * @version 1.0
 */
public class CTCCFormBinder extends BaseFormBinder {

    /**
     * The namespace for this form.
     */
    public static final String NAMESPACE = "_30_";

    /**
     * Creates a new binder.
     */
    public CTCCFormBinder() {
        super(NAMESPACE);
    }

    /**
     * Binds the request to the model.
     *
     * @param enrollment the model to bind to
     * @param request the request containing the form fields
     * @throws BinderException if the format of the fields could not be bound properly
     */
    @SuppressWarnings("unchecked")
    public List<BinderException> bindFromPage(EnrollmentType enrollment, HttpServletRequest request) {
        ProviderInformationType provider = XMLUtility.nsGetProvider(enrollment);
        AttachedDocumentsType attachments = XMLUtility.nsGetAttachments(provider);

        String attachmentId = (String) request.getAttribute(NAMESPACE + "dhsContract");
        if (attachmentId != null) {
            replaceDocument(attachments, attachmentId, DocumentNames.COMMUNITY_HEALTH_BOARD_DHS_CONTRACT.value());
        }
        
        FacilityCredentialsType creds = XMLUtility.nsGetFacilityCredentials(enrollment);
        if (param(request, "chbIndicator") != null) {
            creds.setCommunityHealthBoard("Y");
        } else {
            creds.setCommunityHealthBoard("N");
        }
        
        return Collections.EMPTY_LIST;
    }

    private void replaceDocument(AttachedDocumentsType attachments, String id, String value) {
        List<DocumentType> toRemove = new ArrayList<DocumentType>();
        List<DocumentType> attachment = attachments.getAttachment();
        for (DocumentType doc : attachment) {
            if (id.equals(doc.getObjectId())) {
                doc.setName(value);
            } else if (value.equals(doc.getName())) {
                toRemove.add(doc);
            }
        }
        
        attachments.getAttachment().removeAll(toRemove);
    }

    /**
     * Binds the model to the request attributes.
     *
     * @param enrollment the model to bind from
     * @param mv the model and view to bind to
     * @param readOnly true if the view is read only
     */
    public void bindToPage(EnrollmentType enrollment, Map<String, Object> mv, boolean readOnly) {
        attr(mv, "bound", "Y");
        ProviderInformationType provider = XMLUtility.nsGetProvider(enrollment);
        AttachedDocumentsType attachments = XMLUtility.nsGetAttachments(provider);
        List<DocumentType> attachment = attachments.getAttachment();
        for (DocumentType doc : attachment) {
            if (DocumentNames.COMMUNITY_HEALTH_BOARD_DHS_CONTRACT.value().equals(doc.getName())) {
                attr(mv, "dhsContract", doc.getObjectId());
            }
        }
        
        FacilityCredentialsType creds = XMLUtility.nsGetFacilityCredentials(enrollment);
        attr(mv, "chbIndicator", creds.getCommunityHealthBoard());
    }
    
    /**
     * Captures the error messages related to the form.
     * @param enrollment the enrollment that was validated
     * @param messages the messages to select from
     *
     * @return the list of errors related to the form
     */
    protected List<FormError> selectErrors(EnrollmentType enrollment, StatusMessagesType messages) {
        List<FormError> errors = new ArrayList<FormError>();

        List<StatusMessageType> ruleErrors = messages.getStatusMessage();
        List<StatusMessageType> caughtMessages = new ArrayList<StatusMessageType>();

        synchronized (ruleErrors) {
            for (StatusMessageType ruleError : ruleErrors) {
                int count = errors.size();

                String path = ruleError.getRelatedElementPath();
                if (path == null) {
                    continue;
                }

                if (path.equals("/ProviderInformation/AttachedDocuments/Document[name=\"Community Health Board DHS Contract\"]")) {
                    errors.add(createError("dhsContract", ruleError.getMessage()));
                }

                if (errors.size() > count) { // caught
                    caughtMessages.add(ruleError);
                }
            }

            // so it does not get processed anywhere again
            ruleErrors.removeAll(caughtMessages);
        }

        return errors.isEmpty() ? NO_ERRORS : errors;
    }

    /**
     * Binds the fields of the form to the persistence model.
     *
     * @param enrollment the front end model
     * @param ticket the persistent model
     */
    public void bindToHibernate(EnrollmentType enrollment, Enrollment ticket) {
        FacilityCredentialsType creds = XMLUtility.nsGetFacilityCredentials(enrollment);
        ProviderProfile profile = ticket.getDetails();
        profile.setHealthBoardInd(creds.getCommunityHealthBoard());
    }

    /**
     * Binds the fields of the persistence model to the front end xml.
     *
     * @param ticket the persistent model
     * @param enrollment the front end model
     */
    public void bindFromHibernate(Enrollment ticket, EnrollmentType enrollment) {
        FacilityCredentialsType creds = XMLUtility.nsGetFacilityCredentials(enrollment);
        ProviderProfile profile = ticket.getDetails();
        if (profile != null) {
            creds.setCommunityHealthBoard(profile.getHealthBoardInd());
        }
    }
}
