import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUser } from './user.model';
import { UserIDAndOrgID } from 'app/shared/model/UserIDAndOrgID';
import { IOrganizationUnit, OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IEbPackage } from 'app/shared/model/eb-package.model';

@Injectable({ providedIn: 'root' })
export class UserService {
    private resourceUrl = SERVER_API_URL + 'api/users';
    private resourceUrl1 = SERVER_API_URL + 'api/p/users';

    constructor(private http: HttpClient) {}

    create(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.post<IUser>(this.resourceUrl, user, { observe: 'response' });
    }

    createUserOrg(userOrgId: UserIDAndOrgID): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourceUrl + '/user-org', userOrgId, { observe: 'response' });
    }

    createUserAdmin(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.post<IUser>(this.resourceUrl + '/create-admin', user, { observe: 'response' });
    }

    update(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.put<IUser>(this.resourceUrl, user, { observe: 'response' });
    }

    updateForEbGroupOrg(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.put<any>(this.resourceUrl + '/updateForEbGroupOrg', req, { observe: 'response' });
    }

    updateInfo(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.put<IUser>(`${this.resourceUrl}/updateInfo`, user, { observe: 'response' });
    }

    updateInfoAdmin(user: IUser): Observable<HttpResponse<IUser>> {
        return this.http.put<IUser>(`${this.resourceUrl}/updateInfoAdmin`, user, { observe: 'response' });
    }

    activePackage(user: any): Observable<HttpResponse<IUser>> {
        return this.http.post<any>(`${this.resourceUrl}/active-package`, user, { observe: 'response' });
    }

    activePackageNoSendCrm(user: any): Observable<HttpResponse<IUser>> {
        return this.http.post<any>(`${this.resourceUrl}/active-package-no-send-crm`, user, { observe: 'response' });
    }

    queryListUser(orgId?: string): Observable<HttpResponse<IUser[]>> {
        return this.http.get<IUser[]>(`${this.resourceUrl + '/list-user'}/${orgId}`, { observe: 'response' });
    }

    updateSession(user: any): Observable<HttpResponse<IUser>> {
        return this.http.put<IUser>(`${this.resourceUrl}/change-session`, user, { observe: 'response' });
    }

    find(login: string): Observable<HttpResponse<IUser>> {
        return this.http.get<IUser>(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    findOrgNameByUserId(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl + '/org-name', { params: options, observe: 'response' });
    }

    query(req?: any): Observable<HttpResponse<IUser[]>> {
        const options = createRequestOption(req);
        return this.http.get<IUser[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    queryClient(req?: any): Observable<HttpResponse<IUser[]>> {
        const options = createRequestOption(req);
        return this.http.get<IUser[]>(this.resourceUrl + '/user-client', { params: options, observe: 'response' });
    }

    delete(login: string): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    queryUserSearch(req?: any): Observable<HttpResponse<IUser[]>> {
        const options = createRequestOption(req);
        return this.http.get<IUser[]>(this.resourceUrl + '/user-search', { params: options, observe: 'response' });
    }

    deleteUserAmin(login: string): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl + '/user-admin'}/${login}`, { observe: 'response' });
    }

    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
    }

    getEbGroups(): Observable<any[]> {
        return this.http.get<any[]>(SERVER_API_URL + 'api/users/ebGroups');
    }

    sendMailResetPassword(email?: any): Observable<any> {
        const options = createRequestOption(email);
        return this.http.get<any>(this.resourceUrl1 + '/reset-password', { params: options, observe: 'response' });
    }

    resetPassword(login?: any): Observable<any> {
        const options = createRequestOption(login);
        return this.http.get<any>(this.resourceUrl1 + '/reset-password-expired', { params: options, observe: 'response' });
    }

    getCurrentBookOfUser() {
        return this.http.get<any>(SERVER_API_URL + 'api/users/currentBookOfUser');
    }
}
