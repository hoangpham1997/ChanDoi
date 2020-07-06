export interface ICareerGroup {
    id?: string;
    careerGroupCode?: string;
    careerGroupName?: string;
}

export class CareerGroup implements ICareerGroup {
    constructor(public id?: string, public careerGroupCode?: string, public careerGroupName?: string) {}
}
