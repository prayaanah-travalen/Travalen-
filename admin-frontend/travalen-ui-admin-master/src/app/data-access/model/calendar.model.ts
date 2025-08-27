export interface CalendarModel {
    roomName: string,
    data: CalendarDataModel[]
}

export interface CalendarDataModel {
     status: string,
     roomsToSell: number,
     netBooked:number,
     standardRate: string,
     nonRefundableRate: string,
     weeklyRate: string,
     roomName: string,
     roomId: number
}