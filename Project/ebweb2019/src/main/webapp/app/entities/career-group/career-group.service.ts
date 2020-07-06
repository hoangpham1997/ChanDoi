import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICareerGroup } from 'app/shared/model/career-group.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';

type EntityResponseType = HttpResponse<ICareerGroup>;
type EntityArrayResponseType = HttpResponse<ICareerGroup[]>;

@Injectable({ providedIn: 'root' })
export class CareerGroupService {
    private resourceUrl = SERVER_API_URL + 'api/career-groups';

    constructor(private http: HttpClient) {}

    create(careerGroup: ICareerGroup): Observable<EntityResponseType> {
        return this.http.post<ICareerGroup>(this.resourceUrl, careerGroup, { observe: 'response' });
    }

    update(careerGroup: ICareerGroup): Observable<EntityResponseType> {
        return this.http.put<ICareerGroup>(this.resourceUrl, careerGroup, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<ICareerGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICareerGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getCareerGroups(): Observable<EntityArrayResponseType> {
        return this.http.get<ICareerGroup[]>(this.resourceUrl + '/get-career-groups', {
            observe: 'response'
        });
    }
}
