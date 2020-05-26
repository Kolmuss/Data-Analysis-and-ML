import pandas as pd
import numpy as np


def distance(departure, arrival, df):
    dep_lt = np.radians(df[df['iata_code'] == departure]['latitude'].values[0])
    dep_lg = np.radians(df[df['iata_code'] == departure]['longitude'].values[0])
    arr_lt = np.radians(df[df['iata_code'] == arrival]['latitude'].values[0])
    arr_lg = np.radians(df[df['iata_code'] == arrival]['longitude'].values[0])
    dep_clt = np.cos(dep_lt)
    arr_clt = np.cos(arr_lt)
    dep_slt = np.sin(dep_lt)
    arr_slt = np.sin(arr_lt)
    c_delta = np.cos(arr_lg - dep_lg)
    s_delta = np.sin(arr_lg - dep_lg)
    y = np.sqrt((arr_clt * s_delta) ** 2 + (dep_clt * arr_slt - dep_slt * arr_clt * c_delta) ** 2)
    x = dep_slt * arr_slt + dep_clt * arr_clt * c_delta
    return np.arctan2(y, x) * 6372795 / 1000


airports = pd.read_csv('data/AirportCoordinates.csv')
flights = pd.read_excel('flighttime.xlsx')

dist = []
for i in airports.index:
    dep = airports.iloc[i]['DepartureStationCode'].replace(" ", "")
    arr = airports.iloc[i]['ArrivalStationCode'].replace(" ", "")
    dist.append(distance(dep, arr, airports))

airports.to_excel('data/flightinfo.xlsx')

