export interface RoleModel {
    roleId: number,
    roleName: string
}

export interface UserModel {
    userId?: number,
    emailId: string,
    password?: string,
    firstName: string,
    lastName: string,
    phoneNo: string,
    countryCode: string,
    role: RoleModel[],
    userType?: string,
    status?: string,
    hotelName?:string,
    hotelCode?:number
}