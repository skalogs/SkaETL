import { Box, Grid, makeStyles } from '@material-ui/core';
import React from 'react';
import { Line } from 'react-chartjs-2';
import { useDashboardState } from '../../../utils/dashboard';
import Card from '../../common/Card';


const useStyles = makeStyles(theme => ({
    text: {
        color: "#6282A3",
        fontFamily: "'Open Sans', sans-serif",
        fontSize: 14,
        fontWeight: 400,
        lineHeight: "19px",
        letterSpacing: "0.28px",
        fontStyle: "normal",
        marginBottom: theme.spacing(0.5),
    },
    container: {
        minHeight: 150,
        maxHeight: 385,
    }
}))

const data = {
    labels: [],
    datasets: [
        // {
        //     label: 'My First dataset',
        //     fill: false,
        //     lineTension: 0.1,
        //     backgroundColor: 'rgba(75,192,192,0.4)',
        //     borderColor: 'rgba(75,192,192,1)',
        //     borderCapStyle: 'butt',
        //     borderDash: [],
        //     borderDashOffset: 0.0,
        //     borderJoinStyle: 'miter',
        //     pointBorderColor: 'rgba(75,192,192,1)',
        //     pointBackgroundColor: '#fff',
        //     pointBorderWidth: 1,
        //     pointHoverRadius: 5,
        //     pointHoverBackgroundColor: 'rgba(75,192,192,1)',
        //     pointHoverBorderColor: 'rgba(220,220,220,1)',
        //     pointHoverBorderWidth: 2,
        //     pointRadius: 1,
        //     pointHitRadius: 10,
        //     data: [],
        // }
    ]
};

const TrafficProcess = props => {
    const { isParagraph } = props
    const classes = useStyles()
    const { state: { dataCapture } } = useDashboardState()
    const [chartData, setChartData] = React.useState(data)

    React.useEffect(() => {
        if (dataCapture.data) {
            setChartData(prevData => {
                return {
                    ...prevData,
                    datasets: dataCapture.data.dataProcess.datasets
                }
            })
        }
    }, [dataCapture.data])
    console.log("TRAFFIC PROCESS COMPONENT", dataCapture, chartData)

    return (
        <Grid container>
            <Grid item xs={12}>
                <Card loading={dataCapture ? false : true} title={"Consumer Traffic Process"} link={"See More"} path={"/trafficprocess"} isLink={false} isParagraph={true}>
                    <Box className={classes.container} component="div">
                        <Line data={chartData} height={100} />
                        {/* <Typography variant="subtitle1" component="p" className={classes.text}>
                            18:44:30:658 Retriving list of development files...
                        </Typography>
                        <Typography variant="subtitle1" component="p" className={classes.text}>
                            18:44:45:256 Downloading 100 Deployment files..
                        </Typography>
                        <Typography variant="subtitle1" component="p" className={classes.text}>
                            18:44:30:658 Installing build runtime
                        </Typography>
                        <Typography variant="subtitle1" component="p" className={classes.text}>
                            18:44:30:658 Build runtime installed 325.684 runtime..
                        </Typography>
                        <Typography variant="subtitle1" component="p" className={classes.text}>
                            18:44:30:658 Retriving list of development files...
                        </Typography>
                        <Typography variant="subtitle1" component="p" className={classes.text}>
                            18:44:30:658 Retriving list of development files...
                        </Typography>
                        <Typography variant="subtitle1" component="p" className={classes.text}>
                            18:44:30:658 Retriving list of development files...
                        </Typography> */}
                    </Box>
                </Card>
            </Grid>
        </Grid>
    )
}

export default TrafficProcess
