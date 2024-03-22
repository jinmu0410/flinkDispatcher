
package com.jm.client.sql.gateway.api;

import com.google.gson.reflect.TypeToken;
import com.jm.client.sql.gateway.invoker.*;
import com.jm.client.sql.gateway.model.*;

import java.lang.reflect.Type;
import java.util.*;

public class DefaultApi {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public DefaultApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DefaultApi(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public int getHostIndex() {
        return localHostIndex;
    }

    public void setHostIndex(int hostIndex) {
        this.localHostIndex = hostIndex;
    }

    public String getCustomBaseUrl() {
        return localCustomBaseUrl;
    }

    public void setCustomBaseUrl(String customBaseUrl) {
        this.localCustomBaseUrl = customBaseUrl;
    }

    /**
     * Build call for cancelOperation
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call cancelOperationCall(UUID sessionHandle, UUID operationHandle, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}/operations/{operation_handle}/cancel"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()))
            .replace("{" + "operation_handle" + "}", localVarApiClient.escapeString(operationHandle.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call cancelOperationValidateBeforeCall(UUID sessionHandle, UUID operationHandle, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling cancelOperation(Async)");
        }

        // verify the required parameter 'operationHandle' is set
        if (operationHandle == null) {
            throw new ApiException("Missing the required parameter 'operationHandle' when calling cancelOperation(Async)");
        }

        return cancelOperationCall(sessionHandle, operationHandle, _callback);

    }

    /**
     * 
     * Cancel the operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @return OperationStatusResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public OperationStatusResponseBody cancelOperation(UUID sessionHandle, UUID operationHandle) throws ApiException {
        ApiResponse<OperationStatusResponseBody> localVarResp = cancelOperationWithHttpInfo(sessionHandle, operationHandle);
        return localVarResp.getData();
    }

    /**
     * 
     * Cancel the operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @return ApiResponse&lt;OperationStatusResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<OperationStatusResponseBody> cancelOperationWithHttpInfo(UUID sessionHandle, UUID operationHandle) throws ApiException {
        okhttp3.Call localVarCall = cancelOperationValidateBeforeCall(sessionHandle, operationHandle, null);
        Type localVarReturnType = new TypeToken<OperationStatusResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Cancel the operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call cancelOperationAsync(UUID sessionHandle, UUID operationHandle, final ApiCallback<OperationStatusResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = cancelOperationValidateBeforeCall(sessionHandle, operationHandle, _callback);
        Type localVarReturnType = new TypeToken<OperationStatusResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for closeOperation
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call closeOperationCall(UUID sessionHandle, UUID operationHandle, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}/operations/{operation_handle}/close"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()))
            .replace("{" + "operation_handle" + "}", localVarApiClient.escapeString(operationHandle.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call closeOperationValidateBeforeCall(UUID sessionHandle, UUID operationHandle, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling closeOperation(Async)");
        }

        // verify the required parameter 'operationHandle' is set
        if (operationHandle == null) {
            throw new ApiException("Missing the required parameter 'operationHandle' when calling closeOperation(Async)");
        }

        return closeOperationCall(sessionHandle, operationHandle, _callback);

    }

    /**
     * 
     * Close the operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @return OperationStatusResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public OperationStatusResponseBody closeOperation(UUID sessionHandle, UUID operationHandle) throws ApiException {
        ApiResponse<OperationStatusResponseBody> localVarResp = closeOperationWithHttpInfo(sessionHandle, operationHandle);
        return localVarResp.getData();
    }

    /**
     * 
     * Close the operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @return ApiResponse&lt;OperationStatusResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<OperationStatusResponseBody> closeOperationWithHttpInfo(UUID sessionHandle, UUID operationHandle) throws ApiException {
        okhttp3.Call localVarCall = closeOperationValidateBeforeCall(sessionHandle, operationHandle, null);
        Type localVarReturnType = new TypeToken<OperationStatusResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Close the operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call closeOperationAsync(UUID sessionHandle, UUID operationHandle, final ApiCallback<OperationStatusResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = closeOperationValidateBeforeCall(sessionHandle, operationHandle, _callback);
        Type localVarReturnType = new TypeToken<OperationStatusResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for closeSession
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call closeSessionCall(UUID sessionHandle, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call closeSessionValidateBeforeCall(UUID sessionHandle, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling closeSession(Async)");
        }

        return closeSessionCall(sessionHandle, _callback);

    }

    /**
     * 
     * Closes the specific session.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @return CloseSessionResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public CloseSessionResponseBody closeSession(UUID sessionHandle) throws ApiException {
        ApiResponse<CloseSessionResponseBody> localVarResp = closeSessionWithHttpInfo(sessionHandle);
        return localVarResp.getData();
    }

    /**
     * 
     * Closes the specific session.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @return ApiResponse&lt;CloseSessionResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<CloseSessionResponseBody> closeSessionWithHttpInfo(UUID sessionHandle) throws ApiException {
        okhttp3.Call localVarCall = closeSessionValidateBeforeCall(sessionHandle, null);
        Type localVarReturnType = new TypeToken<CloseSessionResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Closes the specific session.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call closeSessionAsync(UUID sessionHandle, final ApiCallback<CloseSessionResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = closeSessionValidateBeforeCall(sessionHandle, _callback);
        Type localVarReturnType = new TypeToken<CloseSessionResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for completeStatement
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param completeStatementRequestBody  (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call completeStatementCall(UUID sessionHandle, CompleteStatementRequestBody completeStatementRequestBody, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = completeStatementRequestBody;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}/complete-statement"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call completeStatementValidateBeforeCall(UUID sessionHandle, CompleteStatementRequestBody completeStatementRequestBody, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling completeStatement(Async)");
        }

        return completeStatementCall(sessionHandle, completeStatementRequestBody, _callback);

    }

    /**
     * 
     * Get the completion hints for the given statement at the given position.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param completeStatementRequestBody  (optional)
     * @return CompleteStatementResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public CompleteStatementResponseBody completeStatement(UUID sessionHandle, CompleteStatementRequestBody completeStatementRequestBody) throws ApiException {
        ApiResponse<CompleteStatementResponseBody> localVarResp = completeStatementWithHttpInfo(sessionHandle, completeStatementRequestBody);
        return localVarResp.getData();
    }

    /**
     * 
     * Get the completion hints for the given statement at the given position.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param completeStatementRequestBody  (optional)
     * @return ApiResponse&lt;CompleteStatementResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<CompleteStatementResponseBody> completeStatementWithHttpInfo(UUID sessionHandle, CompleteStatementRequestBody completeStatementRequestBody) throws ApiException {
        okhttp3.Call localVarCall = completeStatementValidateBeforeCall(sessionHandle, completeStatementRequestBody, null);
        Type localVarReturnType = new TypeToken<CompleteStatementResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Get the completion hints for the given statement at the given position.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param completeStatementRequestBody  (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call completeStatementAsync(UUID sessionHandle, CompleteStatementRequestBody completeStatementRequestBody, final ApiCallback<CompleteStatementResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = completeStatementValidateBeforeCall(sessionHandle, completeStatementRequestBody, _callback);
        Type localVarReturnType = new TypeToken<CompleteStatementResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for configureSession
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param configureSessionRequestBody  (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call configureSessionCall(UUID sessionHandle, ConfigureSessionRequestBody configureSessionRequestBody, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = configureSessionRequestBody;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}/configure-session"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call configureSessionValidateBeforeCall(UUID sessionHandle, ConfigureSessionRequestBody configureSessionRequestBody, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling configureSession(Async)");
        }

        return configureSessionCall(sessionHandle, configureSessionRequestBody, _callback);

    }

    /**
     * 
     * Configures the session with the statement which could be: CREATE TABLE, DROP TABLE, ALTER TABLE, CREATE DATABASE, DROP DATABASE, ALTER DATABASE, CREATE FUNCTION, DROP FUNCTION, ALTER FUNCTION, CREATE CATALOG, DROP CATALOG, USE CATALOG, USE [CATALOG.]DATABASE, CREATE VIEW, DROP VIEW, LOAD MODULE, UNLOAD MODULE, USE MODULE, ADD JAR.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param configureSessionRequestBody  (optional)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public void configureSession(UUID sessionHandle, ConfigureSessionRequestBody configureSessionRequestBody) throws ApiException {
        configureSessionWithHttpInfo(sessionHandle, configureSessionRequestBody);
    }

    /**
     * 
     * Configures the session with the statement which could be: CREATE TABLE, DROP TABLE, ALTER TABLE, CREATE DATABASE, DROP DATABASE, ALTER DATABASE, CREATE FUNCTION, DROP FUNCTION, ALTER FUNCTION, CREATE CATALOG, DROP CATALOG, USE CATALOG, USE [CATALOG.]DATABASE, CREATE VIEW, DROP VIEW, LOAD MODULE, UNLOAD MODULE, USE MODULE, ADD JAR.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param configureSessionRequestBody  (optional)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<Void> configureSessionWithHttpInfo(UUID sessionHandle, ConfigureSessionRequestBody configureSessionRequestBody) throws ApiException {
        okhttp3.Call localVarCall = configureSessionValidateBeforeCall(sessionHandle, configureSessionRequestBody, null);
        return localVarApiClient.execute(localVarCall);
    }

    /**
     *  (asynchronously)
     * Configures the session with the statement which could be: CREATE TABLE, DROP TABLE, ALTER TABLE, CREATE DATABASE, DROP DATABASE, ALTER DATABASE, CREATE FUNCTION, DROP FUNCTION, ALTER FUNCTION, CREATE CATALOG, DROP CATALOG, USE CATALOG, USE [CATALOG.]DATABASE, CREATE VIEW, DROP VIEW, LOAD MODULE, UNLOAD MODULE, USE MODULE, ADD JAR.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param configureSessionRequestBody  (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call configureSessionAsync(UUID sessionHandle, ConfigureSessionRequestBody configureSessionRequestBody, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = configureSessionValidateBeforeCall(sessionHandle, configureSessionRequestBody, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }
    /**
     * Build call for executeStatement
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param executeStatementRequestBody  (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call executeStatementCall(UUID sessionHandle, ExecuteStatementRequestBody executeStatementRequestBody, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = executeStatementRequestBody;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}/statements"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call executeStatementValidateBeforeCall(UUID sessionHandle, ExecuteStatementRequestBody executeStatementRequestBody, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling executeStatement(Async)");
        }

        return executeStatementCall(sessionHandle, executeStatementRequestBody, _callback);

    }

    /**
     * 
     * Execute a statement.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param executeStatementRequestBody  (optional)
     * @return ExecuteStatementResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ExecuteStatementResponseBody executeStatement(UUID sessionHandle, ExecuteStatementRequestBody executeStatementRequestBody) throws ApiException {
        ApiResponse<ExecuteStatementResponseBody> localVarResp = executeStatementWithHttpInfo(sessionHandle, executeStatementRequestBody);
        return localVarResp.getData();
    }

    /**
     * 
     * Execute a statement.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param executeStatementRequestBody  (optional)
     * @return ApiResponse&lt;ExecuteStatementResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<ExecuteStatementResponseBody> executeStatementWithHttpInfo(UUID sessionHandle, ExecuteStatementRequestBody executeStatementRequestBody) throws ApiException {
        okhttp3.Call localVarCall = executeStatementValidateBeforeCall(sessionHandle, executeStatementRequestBody, null);
        Type localVarReturnType = new TypeToken<ExecuteStatementResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Execute a statement.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param executeStatementRequestBody  (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call executeStatementAsync(UUID sessionHandle, ExecuteStatementRequestBody executeStatementRequestBody, final ApiCallback<ExecuteStatementResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = executeStatementValidateBeforeCall(sessionHandle, executeStatementRequestBody, _callback);
        Type localVarReturnType = new TypeToken<ExecuteStatementResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for fetchResults
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param token The OperationHandle that identifies a operation. (required)
     * @param rowFormat The row format to serialize the RowData. (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call fetchResultsCall(UUID sessionHandle, UUID operationHandle, Long token, RowFormat rowFormat, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}/operations/{operation_handle}/result/{token}"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()))
            .replace("{" + "operation_handle" + "}", localVarApiClient.escapeString(operationHandle.toString()))
            .replace("{" + "token" + "}", localVarApiClient.escapeString(token.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (rowFormat != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("rowFormat", rowFormat));
        }

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call fetchResultsValidateBeforeCall(UUID sessionHandle, UUID operationHandle, Long token, RowFormat rowFormat, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling fetchResults(Async)");
        }

        // verify the required parameter 'operationHandle' is set
        if (operationHandle == null) {
            throw new ApiException("Missing the required parameter 'operationHandle' when calling fetchResults(Async)");
        }

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException("Missing the required parameter 'token' when calling fetchResults(Async)");
        }

        // verify the required parameter 'rowFormat' is set
        if (rowFormat == null) {
            throw new ApiException("Missing the required parameter 'rowFormat' when calling fetchResults(Async)");
        }

        return fetchResultsCall(sessionHandle, operationHandle, token, rowFormat, _callback);

    }

    /**
     * 
     * Fetch results of Operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param token The OperationHandle that identifies a operation. (required)
     * @param rowFormat The row format to serialize the RowData. (required)
     * @return FetchResultsResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public FetchResultsResponseBody fetchResults(UUID sessionHandle, UUID operationHandle, Long token, RowFormat rowFormat) throws ApiException {
        ApiResponse<FetchResultsResponseBody> localVarResp = fetchResultsWithHttpInfo(sessionHandle, operationHandle, token, rowFormat);
        return localVarResp.getData();
    }

    /**
     * 
     * Fetch results of Operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param token The OperationHandle that identifies a operation. (required)
     * @param rowFormat The row format to serialize the RowData. (required)
     * @return ApiResponse&lt;FetchResultsResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<FetchResultsResponseBody> fetchResultsWithHttpInfo(UUID sessionHandle, UUID operationHandle, Long token, RowFormat rowFormat) throws ApiException {
        okhttp3.Call localVarCall = fetchResultsValidateBeforeCall(sessionHandle, operationHandle, token, rowFormat, null);
        Type localVarReturnType = new TypeToken<FetchResultsResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Fetch results of Operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param token The OperationHandle that identifies a operation. (required)
     * @param rowFormat The row format to serialize the RowData. (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call fetchResultsAsync(UUID sessionHandle, UUID operationHandle, Long token, RowFormat rowFormat, final ApiCallback<FetchResultsResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = fetchResultsValidateBeforeCall(sessionHandle, operationHandle, token, rowFormat, _callback);
        Type localVarReturnType = new TypeToken<FetchResultsResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for getApiVersion
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getApiVersionCall(final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api_versions";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getApiVersionValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return getApiVersionCall(_callback);

    }

    /**
     * 
     * Get the current available versions for the Rest Endpoint. The client can choose one of the return version as the protocol for later communicate.
     * @return GetApiVersionResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public GetApiVersionResponseBody getApiVersion() throws ApiException {
        ApiResponse<GetApiVersionResponseBody> localVarResp = getApiVersionWithHttpInfo();
        return localVarResp.getData();
    }

    /**
     * 
     * Get the current available versions for the Rest Endpoint. The client can choose one of the return version as the protocol for later communicate.
     * @return ApiResponse&lt;GetApiVersionResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<GetApiVersionResponseBody> getApiVersionWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = getApiVersionValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<GetApiVersionResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Get the current available versions for the Rest Endpoint. The client can choose one of the return version as the protocol for later communicate.
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getApiVersionAsync(final ApiCallback<GetApiVersionResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = getApiVersionValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<GetApiVersionResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for getInfo
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getInfoCall(final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/info";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getInfoValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return getInfoCall(_callback);

    }

    /**
     * 
     * Get meta data for this cluster.
     * @return GetInfoResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public GetInfoResponseBody getInfo() throws ApiException {
        ApiResponse<GetInfoResponseBody> localVarResp = getInfoWithHttpInfo();
        return localVarResp.getData();
    }

    /**
     * 
     * Get meta data for this cluster.
     * @return ApiResponse&lt;GetInfoResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<GetInfoResponseBody> getInfoWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = getInfoValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<GetInfoResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Get meta data for this cluster.
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getInfoAsync(final ApiCallback<GetInfoResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = getInfoValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<GetInfoResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for getOperationStatus
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getOperationStatusCall(UUID sessionHandle, UUID operationHandle, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}/operations/{operation_handle}/status"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()))
            .replace("{" + "operation_handle" + "}", localVarApiClient.escapeString(operationHandle.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getOperationStatusValidateBeforeCall(UUID sessionHandle, UUID operationHandle, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling getOperationStatus(Async)");
        }

        // verify the required parameter 'operationHandle' is set
        if (operationHandle == null) {
            throw new ApiException("Missing the required parameter 'operationHandle' when calling getOperationStatus(Async)");
        }

        return getOperationStatusCall(sessionHandle, operationHandle, _callback);

    }

    /**
     * 
     * Get the status of operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @return OperationStatusResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public OperationStatusResponseBody getOperationStatus(UUID sessionHandle, UUID operationHandle) throws ApiException {
        ApiResponse<OperationStatusResponseBody> localVarResp = getOperationStatusWithHttpInfo(sessionHandle, operationHandle);
        return localVarResp.getData();
    }

    /**
     * 
     * Get the status of operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @return ApiResponse&lt;OperationStatusResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<OperationStatusResponseBody> getOperationStatusWithHttpInfo(UUID sessionHandle, UUID operationHandle) throws ApiException {
        okhttp3.Call localVarCall = getOperationStatusValidateBeforeCall(sessionHandle, operationHandle, null);
        Type localVarReturnType = new TypeToken<OperationStatusResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Get the status of operation.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param operationHandle The OperationHandle that identifies a operation. (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getOperationStatusAsync(UUID sessionHandle, UUID operationHandle, final ApiCallback<OperationStatusResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = getOperationStatusValidateBeforeCall(sessionHandle, operationHandle, _callback);
        Type localVarReturnType = new TypeToken<OperationStatusResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for getSessionConfig
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getSessionConfigCall(UUID sessionHandle, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getSessionConfigValidateBeforeCall(UUID sessionHandle, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling getSessionConfig(Async)");
        }

        return getSessionConfigCall(sessionHandle, _callback);

    }

    /**
     * 
     * Get the session configuration.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @return GetSessionConfigResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public GetSessionConfigResponseBody getSessionConfig(UUID sessionHandle) throws ApiException {
        ApiResponse<GetSessionConfigResponseBody> localVarResp = getSessionConfigWithHttpInfo(sessionHandle);
        return localVarResp.getData();
    }

    /**
     * 
     * Get the session configuration.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @return ApiResponse&lt;GetSessionConfigResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<GetSessionConfigResponseBody> getSessionConfigWithHttpInfo(UUID sessionHandle) throws ApiException {
        okhttp3.Call localVarCall = getSessionConfigValidateBeforeCall(sessionHandle, null);
        Type localVarReturnType = new TypeToken<GetSessionConfigResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Get the session configuration.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call getSessionConfigAsync(UUID sessionHandle, final ApiCallback<GetSessionConfigResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = getSessionConfigValidateBeforeCall(sessionHandle, _callback);
        Type localVarReturnType = new TypeToken<GetSessionConfigResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for openSession
     * @param openSessionRequestBody  (optional)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call openSessionCall(OpenSessionRequestBody openSessionRequestBody, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = openSessionRequestBody;

        // create path and map variables
        String localVarPath = "/sessions";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call openSessionValidateBeforeCall(OpenSessionRequestBody openSessionRequestBody, final ApiCallback _callback) throws ApiException {
        return openSessionCall(openSessionRequestBody, _callback);

    }

    /**
     * 
     * Opens a new session with specific properties. Specific properties can be given for current session which will override the default properties of gateway.
     * @param openSessionRequestBody  (optional)
     * @return OpenSessionResponseBody
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public OpenSessionResponseBody openSession(OpenSessionRequestBody openSessionRequestBody) throws ApiException {
        ApiResponse<OpenSessionResponseBody> localVarResp = openSessionWithHttpInfo(openSessionRequestBody);
        return localVarResp.getData();
    }

    /**
     * 
     * Opens a new session with specific properties. Specific properties can be given for current session which will override the default properties of gateway.
     * @param openSessionRequestBody  (optional)
     * @return ApiResponse&lt;OpenSessionResponseBody&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<OpenSessionResponseBody> openSessionWithHttpInfo(OpenSessionRequestBody openSessionRequestBody) throws ApiException {
        okhttp3.Call localVarCall = openSessionValidateBeforeCall(openSessionRequestBody, null);
        Type localVarReturnType = new TypeToken<OpenSessionResponseBody>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     *  (asynchronously)
     * Opens a new session with specific properties. Specific properties can be given for current session which will override the default properties of gateway.
     * @param openSessionRequestBody  (optional)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call openSessionAsync(OpenSessionRequestBody openSessionRequestBody, final ApiCallback<OpenSessionResponseBody> _callback) throws ApiException {

        okhttp3.Call localVarCall = openSessionValidateBeforeCall(openSessionRequestBody, _callback);
        Type localVarReturnType = new TypeToken<OpenSessionResponseBody>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
    /**
     * Build call for triggerSession
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call triggerSessionCall(UUID sessionHandle, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/sessions/{session_handle}/heartbeat"
            .replace("{" + "session_handle" + "}", localVarApiClient.escapeString(sessionHandle.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] {  };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call triggerSessionValidateBeforeCall(UUID sessionHandle, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'sessionHandle' is set
        if (sessionHandle == null) {
            throw new ApiException("Missing the required parameter 'sessionHandle' when calling triggerSession(Async)");
        }

        return triggerSessionCall(sessionHandle, _callback);

    }

    /**
     * 
     * Trigger heartbeat to tell the server that the client is active, and to keep the session alive as long as configured timeout value.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public void triggerSession(UUID sessionHandle) throws ApiException {
        triggerSessionWithHttpInfo(sessionHandle);
    }

    /**
     * 
     * Trigger heartbeat to tell the server that the client is active, and to keep the session alive as long as configured timeout value.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public ApiResponse<Void> triggerSessionWithHttpInfo(UUID sessionHandle) throws ApiException {
        okhttp3.Call localVarCall = triggerSessionValidateBeforeCall(sessionHandle, null);
        return localVarApiClient.execute(localVarCall);
    }

    /**
     *  (asynchronously)
     * Trigger heartbeat to tell the server that the client is active, and to keep the session alive as long as configured timeout value.
     * @param sessionHandle The SessionHandle that identifies a session. (required)
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The request was successful. </td><td>  -  </td></tr>
     </table>
     */
    public okhttp3.Call triggerSessionAsync(UUID sessionHandle, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = triggerSessionValidateBeforeCall(sessionHandle, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }
}
