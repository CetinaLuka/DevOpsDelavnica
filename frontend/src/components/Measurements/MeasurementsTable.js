import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import DoneIcon from '@mui/icons-material/Done';
import CloseIcon from '@mui/icons-material/Close';

export default function MeasurementsTable({ measurements }) {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 350 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>id</TableCell>
            <TableCell align="right" id="measurementsTableHeaderDate"><b>date</b></TableCell>
            <TableCell align="right" id="measurementsTableHeaderId"><b>product id</b></TableCell>
            <TableCell align="right" id="measurementsTableHeaderTempOK"><b>is OK</b></TableCell>
            <TableCell align="right" id="measurementsTableHeaderAverage"><b>AVG Temp</b></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {measurements.map((row) => (
            <TableRow
              key={row.id}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {row.id}
              </TableCell>
              <TableCell align="right" id={`measurementsTableBodyDate${row.id}`} name="measurementsTableBodyDate">{row.date}</TableCell>
              <TableCell align="right" id={`measurementsTableBodyProductId${row.id}`} name="measurementsTableBodyProductId">{row.productId}</TableCell>
              <TableCell align="right" id={`measurementsTableBodyTempOK${row.id}`} name="measurementsTableBodyTempOK">{row.isOk ? <DoneIcon sx={{ color: 'green' }} /> : <CloseIcon sx={{ color: 'red' }} />}</TableCell>
              <TableCell align="right" id={`measurementsTableBodyAverage${row.id}`} name="measurementsTableBodyAverage">{row.avgTemperature}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}